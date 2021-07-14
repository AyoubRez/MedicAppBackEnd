package com.mdc.medic.apimedic.services;

import com.mdc.medic.apimedic.config.VersionConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicService {
    private final Logger logger = LogManager.getLogger(getClass());

    @Autowired
    VersionConfig versionConfig;


    public Boolean isMobileVersionCompatible(String mobileVersion, String platform ) {
        logger.info("Start is Mobile Version compatible '{}'", mobileVersion);

        Boolean isCompatible=null;

        if("android".equalsIgnoreCase(platform)){
            isCompatible = versionConfig.getCompatibleAndroid().contains(mobileVersion);
        }else if ("ios".equalsIgnoreCase(platform)){
            isCompatible = versionConfig.getCompatibleIos().contains(mobileVersion);
        }else{
            isCompatible = versionConfig.getCompatibleAndroid().concat(versionConfig.getCompatibleIos()).contains(mobileVersion);
        }

        logger.debug("End is Mobile Version compatible");
        return isCompatible;
    }
}
