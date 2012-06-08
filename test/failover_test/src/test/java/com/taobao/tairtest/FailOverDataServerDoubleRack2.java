/**
 * 
 */
package com.taobao.tairtest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FailOverDataServerDoubleRack2 extends FailOverBaseCase{

	@Test
	public void testFailover_05_kill_more_servers_on_master_rack()
	{ 
		log.error("start DataServer test Failover case 05");
		int waitcnt=0;
		if(!control_cluster(csList, dsList, FailOverBaseCase.start, 0))fail("start cluster failed!");
		log.error("wait system initialize ...");
		waitto(down_time);
		log.error("Start Cluster Successful!");

		//change test tool's configuration
		if(!modify_config_file("local", test_bin+toolconf, "actiontype", "put"))
			fail("modify configure file failed");
		if(!modify_config_file("local", test_bin+toolconf, "datasize", put_count))
			fail("modify configure file failed");
		if(!modify_config_file("local", test_bin+toolconf, "filename", "read.kv"))
			fail("modify configure file failed");

		execute_data_verify_tool();

		//check verify
		while(check_process("local", toolname)!=2)
		{
			waitto(2);
			if(++waitcnt>150)break;
		}	

		if(waitcnt>150)fail("put data time out!");
		waitcnt=0;

		//verify get result
		int datacnt=getVerifySuccessful();
		assertTrue("put successful rate small than 90%!",datacnt/put_count_float>0.9);
		log.error("Write data over!");

		//wait for duplicate
		waitto(10);

		//close more data server in master rack 
		if(!control_ds((String) dsList.get(0), FailOverBaseCase.stop, 0))
			fail("close data server failed!");
		if(!control_ds((String) dsList.get(2), FailOverBaseCase.stop, 0))
			fail("close data server failed!");
		log.error("all data server in master rack have been closed!");
		waitto(ds_down_time);

		//check migration stat of start
		while(check_keyword((String) csList.get(0), start_migrate, tair_bin+"logs/config.log")==0)
		{
			waitto(2);
			if(++waitcnt>10)break;
		}
		if(waitcnt>10)fail("check migrate time out!");
		waitcnt=0;
		log.error("check migrate started!");

		//start the 2 data server again
		if(!control_ds((String) dsList.get(0), FailOverBaseCase.start, 0))
			fail("start data server failed!");
		if(!control_ds((String) dsList.get(2), FailOverBaseCase.start, 0))
			fail("start data server failed!");
		log.error("all data server in master rack have been started!");
		waitto(down_time);

		//change test tool's configuration
		if(!modify_config_file("local", test_bin+toolconf, "actiontype", "get"))
			fail("modify configure file failed");	
		//read data from cluster
		execute_data_verify_tool();
		//check verify
		while(check_process("local", toolname)!=2)
		{
			waitto(2);
			if(++waitcnt>150)break;
		}	
		if(waitcnt>150)fail("Read data time out!");
		waitcnt=0;
		log.error("Read data over!");

		//verify get result
		assertEquals("verify data failed!", datacnt, getVerifySuccessful());
		log.error("Successfully Verified data!");

		//end test
		log.error("end DataServer test Failover case 05");
	}

	@Test
	public void testFailover_25_kill_more_servers_on_slave_rack()
	{ 
		log.error("start DataServer test Failover case 25");
		int waitcnt=0;
		if(!control_cluster(csList, dsList, FailOverBaseCase.start, 0))fail("start cluster failed!");
		log.error("wait system initialize ...");
		waitto(down_time);
		log.error("Start Cluster Successful!");

		//change test tool's configuration
		if(!modify_config_file("local", test_bin+toolconf, "actiontype", "put"))
			fail("modify configure file failed");
		if(!modify_config_file("local", test_bin+toolconf, "datasize", put_count))
			fail("modify configure file failed");
		if(!modify_config_file("local", test_bin+toolconf, "filename", "read.kv"))
			fail("modify configure file failed");

		execute_data_verify_tool();

		//check verify
		while(check_process("local", toolname)!=2)
		{
			waitto(2);
			if(++waitcnt>150)break;
		}	

		if(waitcnt>150)fail("put data time out!");
		waitcnt=0;

		//verify get result
		int datacnt=getVerifySuccessful();
		assertTrue("put successful rate small than 90%!",datacnt/put_count_float>0.9);
		log.error("Write data over!");

		//wait for duplicate
		waitto(10);

		//close more data server in master rack 
		if(!control_ds((String) dsList.get(1), FailOverBaseCase.stop, 0))
			fail("close data server failed!");
		if(!control_ds((String) dsList.get(3), FailOverBaseCase.stop, 0))
			fail("close data server failed!");
		log.error("all data server in slave rack have been closed!");
		waitto(ds_down_time);

		//check migration stat of start
		while(check_keyword((String) csList.get(0), start_migrate, tair_bin+"logs/config.log")==0)
		{
			waitto(2);
			if(++waitcnt>10)break;
		}
		if(waitcnt>10)fail("check migrate time out!");
		waitcnt=0;
		log.error("check migrate started!");

		//start the 2 data server again
		if(!control_ds((String) dsList.get(1), FailOverBaseCase.start, 0))
			fail("start data server failed!");
		if(!control_ds((String) dsList.get(3), FailOverBaseCase.start, 0))
			fail("start data server failed!");
		log.error("all data server in slave rack have been started!");
		//wait for data server start
		waitto(down_time);

		//change test tool's configuration
		if(!modify_config_file("local", test_bin+toolconf, "actiontype", "get"))
			fail("modify configure file failed");	
		//read data from cluster
		execute_data_verify_tool();
		//check verify
		while(check_process("local", toolname)!=2)
		{
			waitto(2);
			if(++waitcnt>150)break;
		}	
		if(waitcnt>150)fail("Read data time out!");
		waitcnt=0;
		log.error("Read data over!");

		//verify get result
		assertEquals("verify data failed!", datacnt, getVerifySuccessful());
		log.error("Successfully Verified data!");

		//end test
		log.error("end DataServer test Failover case 25");
	}

	@Test
	public void testFailover_26_kill_all_servers_on_master_rack()
	{ 
		log.error("start DataServer test Failover case 25");
		int waitcnt=0;
		if(!control_cluster(csList, dsList, FailOverBaseCase.start, 0))fail("start cluster failed!");
		log.error("wait system initialize ...");
		waitto(down_time);
		log.error("Start Cluster Successful!");

		//change test tool's configuration
		if(!modify_config_file("local", test_bin+toolconf, "actiontype", "put"))
			fail("modify configure file failed");
		if(!modify_config_file("local", test_bin+toolconf, "datasize", put_count))
			fail("modify configure file failed");
		if(!modify_config_file("local", test_bin+toolconf, "filename", "read.kv"))
			fail("modify configure file failed");

		execute_data_verify_tool();

		//check verify
		while(check_process("local", toolname)!=2)
		{
			waitto(2);
			if(++waitcnt>150)break;
		}	

		if(waitcnt>150)fail("put data time out!");
		waitcnt=0;

		//verify get result
		int datacnt=getVerifySuccessful();
		assertTrue("put successful rate small than 90%!",datacnt/put_count_float>0.9);
		log.error("Write data over!");

		//wait for duplicate
		waitto(10);

		//close all data server in master rack 
		for(int i=0; i<dsList.size(); i+=2)
		{
			if(!control_ds((String) dsList.get(i), FailOverBaseCase.stop, 0))
				fail("close data server failed!");
			waitto(ds_down_time);
		}
		log.error("all data server in master rack have been closed!");

		//check migration stat of start
		while(check_keyword((String) csList.get(0), start_migrate, tair_bin+"logs/config.log")!=dsList.size()/2)
		{
			waitto(2);
			if(++waitcnt>10)break;
		}
		if(waitcnt>10)fail("check migrate time out!");
		waitcnt=0;
		log.error("check migrate started!");

		//start the 2 data server again
		for(int j=0; j<dsList.size(); j+=2)
		{
			if(!control_ds((String) dsList.get(j), FailOverBaseCase.start, 0))
				fail("start data server failed!");
		}
		log.error("all data server in master rack have been started!");
		waitto(down_time);

		//change test tool's configuration
		if(!modify_config_file("local", test_bin+toolconf, "actiontype", "get"))
			fail("modify configure file failed");	
		//read data from cluster
		execute_data_verify_tool();
		//check verify
		while(check_process("local", toolname)!=2)
		{
			waitto(2);
			if(++waitcnt>150)break;
		}	
		if(waitcnt>150)fail("Read data time out!");
		waitcnt=0;
		log.error("Read data over!");

		//verify get result
		assertEquals("verify data failed!", datacnt, getVerifySuccessful());
		log.error("Successfully Verified data!");

		//end test
		log.error("end DataServer test Failover case 26");
	}

	@Test
	public void testFailover_27_kill_all_servers_on_slave_rack()
	{ 
		log.error("start DataServer test Failover case 27");
		int waitcnt=0;
		if(!control_cluster(csList, dsList, FailOverBaseCase.start, 0))fail("start cluster failed!");
		log.error("wait system initialize ...");
		waitto(down_time);
		log.error("Start Cluster Successful!");

		//change test tool's configuration
		if(!modify_config_file("local", test_bin+toolconf, "actiontype", "put"))
			fail("modify configure file failed");
		if(!modify_config_file("local", test_bin+toolconf, "datasize", put_count))
			fail("modify configure file failed");
		if(!modify_config_file("local", test_bin+toolconf, "filename", "read.kv"))
			fail("modify configure file failed");

		execute_data_verify_tool();

		//check verify
		while(check_process("local", toolname)!=2)
		{
			waitto(2);
			if(++waitcnt>150)break;
		}	

		if(waitcnt>150)fail("put data time out!");
		waitcnt=0;

		//verify get result
		int datacnt=getVerifySuccessful();
		assertTrue("put successful rate small than 90%!",datacnt/put_count_float>0.9);
		log.error("Write data over!");

		//wait for duplicate
		waitto(10);

		//close all data server in master rack 
		for(int i=1; i<dsList.size(); i+=2)
		{
			if(!control_ds((String) dsList.get(i), FailOverBaseCase.stop, 0))
				fail("close data server failed!");
			waitto(ds_down_time);
		}
		log.error("all data server in slave rack have been closed!");

		//check migration stat of start
		while(check_keyword((String) csList.get(0), start_migrate, tair_bin+"logs/config.log")!=dsList.size()/2)
		{
			waitto(2);
			if(++waitcnt>10)break;
		}
		if(waitcnt>10)fail("check migrate time out!");
		waitcnt=0;
		log.error("check migrate started!");

		//start the  2 data server again
		for(int j=1; j<dsList.size(); j+=2)
		{
			if(!control_ds((String) dsList.get(j), FailOverBaseCase.start, 0))
				fail("start data server failed!");
		}
		log.error("all data server in slave rack have been started!");
		waitto(down_time);

		//change test tool's configuration
		if(!modify_config_file("local", test_bin+toolconf, "actiontype", "get"))
			fail("modify configure file failed");	
		//read data from cluster
		execute_data_verify_tool();
		//check verify
		while(check_process("local", toolname)!=2)
		{
			waitto(2);
			if(++waitcnt>150)break;
		}	
		if(waitcnt>150)fail("Read data time out!");
		waitcnt=0;
		log.error("Read data over!");

		//verify get result
		assertEquals("verify data failed!", datacnt, getVerifySuccessful());
		log.error("Successfully Verified data!");

		//end test
		log.error("end DataServer test Failover case 27");
	}
	
	@Before
	public void setUp()
	{
		log.error("clean tool and cluster!");
		clean_tool("local");
		reset_cluster(csList,dsList);
		if(!batch_modify(csList, tair_bin+"etc/group.conf", "_copy_count", "2"))
            fail("modify configure file failure");
        if(!batch_modify(dsList, tair_bin+"etc/group.conf", "_copy_count", "2"))
            fail("modify configure file failure");
	}
	
	@After
	public void tearDown()
	{
		clean_tool("local");
		log.error("clean tool and cluster!");
		batch_uncomment(csList, tair_bin+"etc/group.conf", dsList, "#");
	}
}