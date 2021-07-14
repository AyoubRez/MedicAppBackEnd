package com.mdc.medic.apimedic.controllers;


import com.mdc.medic.apimedic.beans.request.version.MobileVersionBean;
import com.mdc.medic.apimedic.exceptions.FunctionalException;
import com.mdc.medic.apimedic.exceptions.MedicFrontExceptionCode;
import com.mdc.medic.apimedic.exceptions.TechnicalException;
import com.mdc.medic.apimedic.services.PublicService;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/public")
@Api(tags="Public")
public class PublicController extends BaseController{
    Logger logger = LogManager.getLogger(getClass());

    @Autowired
    PublicService publicService;

    @RequestMapping(method = RequestMethod.POST, path= "/version")
    public Boolean isMobileVersionCompatible(HttpServletRequest request, @RequestBody MobileVersionBean mobileVersion) throws TechnicalException, FunctionalException {
        logger.info("Start is Mobile Version Compatible Version:'{}'", mobileVersion);

        Boolean isCompatibleVersion = publicService.isMobileVersionCompatible(mobileVersion.getVersion(), mobileVersion.getPlatform());

        if(!isCompatibleVersion) {
            throw new FunctionalException(MedicFrontExceptionCode.NOT_COMPATIBLE_VERSION, "NOT_COMPATIBLE_VERSION");
        }

        logger.info("End isMobileVersionCompatible with '{}'", isCompatibleVersion);
        return isCompatibleVersion;

    }
}
