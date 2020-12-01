package eu.profinit.education.flightlog.dao;

import eu.profinit.education.flightlog.exceptions.ExternalSystemException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!stub")
public class ClubDatabaseDaoImpl implements ClubDatabaseDao {

    private final RestTemplate restTemplate;
    private final String clubDbBaseUrl;

    private final static String USERS = "/club/user";

    public ClubDatabaseDaoImpl(@Value("${integration.clubDb.baseUrl}") String clubBaseUrl) {
        this.restTemplate = new RestTemplate();
        this.clubDbBaseUrl = clubBaseUrl;
    }


    @Override
    public List<User> getUsers() {
        User[] userList;
        try {
            userList = restTemplate.getForObject(clubDbBaseUrl + USERS, User[].class);
        } catch (RuntimeException e) {
            throw new ExternalSystemException("Cannot get users from Club database. URL: {}. Call resulted in exception.", e, clubDbBaseUrl);
        }
        return Arrays.asList(userList);
    }
}
