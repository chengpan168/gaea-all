package com.gaea.common.sms;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PhoneMessageInterfaceFactory {
	
	public static final Logger logger = LoggerFactory.getLogger(PhoneMessageInterfaceFactory.class);
	

	public static final String DEFAULT_PHONE_MESSAGE_TYPE = "luosimao";
	
	private static Map<String,PhoneMessageInterface> configMessage;


	public static PhoneMessageInterface getDefaultPhoneMessageInterface() {
		return getPhoneMessageInterface(DEFAULT_PHONE_MESSAGE_TYPE);
	}

	public static PhoneMessageInterface getPhoneMessageInterface(String type) {
		if (StringUtils.isEmpty(type)) {
			logger.debug("type is null!");
			throw new IllegalArgumentException("type is null");
		}

		PhoneMessageInterface messageInterface = configMessage.get(type);

		if (messageInterface == null) {
			logger.debug("get PhoneMessageInterface is null , type : {}",type);
            throw new IllegalArgumentException("get PhoneMessageInterface is null , type : {"+type+"}");
		}

		return messageInterface;
	}

	public  void setConfigMessage(
			Map<String, PhoneMessageInterface> configMessage) {
		PhoneMessageInterfaceFactory.configMessage = configMessage;
	}
 
}
