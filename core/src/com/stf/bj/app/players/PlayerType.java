package com.stf.bj.app.players;

import java.util.Random;

public enum PlayerType {
	HUMAN, BASIC_BOT, BASIC_COUNTING_BOT, BASIC_INDEX_COUNTING_BOT;

	public static PlayerType getRandom() {
		Random r = new Random(System.currentTimeMillis());
		int type = r.nextInt(3);
		if(type == 0) {
			return BASIC_BOT;
		}else if(type==1){
			return BASIC_COUNTING_BOT;
		}else if(type==2) {
			return BASIC_INDEX_COUNTING_BOT;
		}
		
		return null;
	}
}
