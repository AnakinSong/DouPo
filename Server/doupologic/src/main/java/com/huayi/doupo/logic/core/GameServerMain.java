package com.huayi.doupo.logic.core;

import com.huayi.doupo.activity.ThreadManager;
import com.huayi.doupo.base.model.dict.DictData;
import com.huayi.doupo.base.util.logic.system.LogUtil;
import com.huayi.doupo.base.util.logic.system.SysConfigUtil;
import com.huayi.doupo.logic.core.tcp.GameServer;
import com.huayi.doupo.logic.handler.util.MineUtil;
import com.huayi.doupo.logic.handler.util.NPCPlayerUtil;
import com.huayi.doupo.logic.handler.util.ScheduleUtil;
import com.huayi.doupo.logic.handler.util.ThreadUtil;
import com.huayi.doupo.logic.handler.util.marquee.AcrossDayThread;
import com.huayi.doupo.logic.handler.util.marquee.MarqueeUtil;
import com.huayi.doupo.logic.handler.util.unionwar.UnionWarUtil;
import com.huayi.doupo.logic.http.HttpServer4Login;
import com.huayi.doupo.logic.util.InitUtil;
import com.huayi.doupo.logic.util.LoadResourceUtil;
import com.huayi.doupo.logic.util.luafight.LuaFightEngine;

/**
 * 游戏主函数
 * 
 * @author mp
 * @date 2014-7-30 下午5:06:00
 */
public class GameServerMain {

	public static void main(String[] args) {

		try {

			// Start RMI Remoting Server
			// RmiStart.startRmiServer();

			// Load Local Resource
			LoadResourceUtil.loadResource();

			// Load Dict Resources
			DictData.loadDictData();
			
			// Load Activity Data From Activity Server
			InitUtil.loadDataFromActivityServer();

			// Init Stroger Hero Jifen
			InitUtil.initStrogerHeroJifen();
			
			//Init Limit Time Hero
			InitUtil.initLimiHeroJifen();
			
			//Init Group
			InitUtil.initGroup();
			
			//Init Exchange
			InitUtil.initExchange();
			
			//Init WorldBoss Info
			InitUtil.initWorldBossInfo();
			
			// 启动时必须要做矿检测，主要功能检测是否结算，或还原天气
			MineUtil.createMine();
			// Luck turntable
			// ActivityLuckManager.getInstance().init();

			// Load RandomNameLibary
			// RandomNameUtil.initRandomNameUtil();

			// Load 5000 NPCPlayer For Duel
			NPCPlayerUtil.loadNPCPlayer();

			// Start Quartz Server
			QuartzServer.start();

			// Marquee
			MarqueeUtil.marquee();

			// Start Statics Thread
			ThreadUtil.startStaticThread();
			
			// 跨天发在线玩家给登录服
			new AcrossDayThread().start();

			// 启动调度器-最强强者发奖调度器
			ScheduleUtil.startAll();

			// 战斗Lua引擎初始化
			LuaFightEngine.init();
			
			// 盟战初始化
			UnionWarUtil.init();

			// Start Game Server
			GameServer.start();

			// Start Recharge Server
			// RechargeServer.start();

			int port = Integer.valueOf(SysConfigUtil.getValue("socketRecharge.port"));
			HttpServer4Login.startServer(port);

			ThreadManager.initThread();
			
			LogUtil.out(" 2015-12-23 12:54  --version-- ++++---- 宙哥提的圣诞活动过期处理数据");
			
//			List<InstBlobData> instActivityList = DALFactory.getInstBlobDataDAL().getList(" type = " + IBlobDataType.TYPE_1_ACTIVITY_LUCK + " limit 0,1", 0);
//			System.out.println("instActivityList.size()="+instActivityList.size());
//			if (instActivityList.size() > 0) {
//				ActivityLuckDataManager.setRankDatas(instActivityList.get(0).getDatas());
//			}
		} catch (Exception e) {
			System.out.println("game server started failed ! ");
			LogUtil.error(e);
			e.printStackTrace();
		}
	}

}
