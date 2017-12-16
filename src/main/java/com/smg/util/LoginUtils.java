package com.smg.util;

import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.smg.util.CommonUtils;
import java.io.IOException;
import org.json.JSONObject;

public class LoginUtils {

    private static final String CREDENTIALS_FILE = CommonUtils.CREDENTIALS_FILE;

    public static MetadataConnection login()
      throws ConnectionException, IOException {
        final JSONObject jsonObj = new JSONObject(CommonUtils.readAllLine(CREDENTIALS_FILE));

        final String username = CommonUtils.getUsername(jsonObj);
        final String password = CommonUtils.getPassword(jsonObj);
        final String orgType = CommonUtils.getOrgType(jsonObj);
        final Double apiVersion = CommonUtils.getApiVersion(jsonObj);
        final String authEndPoint = CommonUtils.getAuthEndPoint(orgType, apiVersion);

        final LoginResult loginResult = loginToSalesforce(username, password, authEndPoint);
        System.out.println("userName: " + loginResult.getUserInfo().getUserName());
        System.out.println("sessionId: " + loginResult.getSessionId());

        final MetadataConnection metadataConnection = createMetadataConnection(loginResult);
        return metadataConnection;
    }

    private static LoginResult loginToSalesforce(final String username, final String password, final String authEndPoint)
      throws ConnectionException {
        final ConnectorConfig config = new ConnectorConfig();
        config.setAuthEndpoint(authEndPoint);
        config.setServiceEndpoint(authEndPoint);
        config.setManualLogin(true);

        final PartnerConnection partnerConnection = new PartnerConnection(config);
        return partnerConnection.login(username, password);
    }

    private static MetadataConnection createMetadataConnection(final LoginResult loginResult)
      throws ConnectionException {
        final ConnectorConfig config = new ConnectorConfig();
        config.setServiceEndpoint(loginResult.getMetadataServerUrl());
        config.setSessionId(loginResult.getSessionId());

        final MetadataConnection metadataConnection = new MetadataConnection(config);
        return metadataConnection;
    }

}