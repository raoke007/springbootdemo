package com.raoke007.springinaction;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>description<p>
 *
 * @author raoke007
 * @date 2019/3/12 16:26
 */
@Repository
public class ContactRepository {

    private final JdbcTemplate jdbcTemplate;

    public ContactRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Contact> findAll() {
        return jdbcTemplate.query("select id, firstName, lastName, phoneNumber, " +
                        "emailAddress from contacts order by lastName",
                new RowMapper<Contact>() {
                    @Override
                    public Contact mapRow(ResultSet resultSet, int i) throws SQLException {
                        return getContact(resultSet);
                    }
                });
    }

    private Contact getContact(ResultSet resultSet) throws SQLException {
        final Contact contact = new Contact();
        contact.setId(resultSet.getLong(1));
        contact.setFirstName(resultSet.getString(2));
        contact.setLastName(resultSet.getString(3));
        contact.setPhoneNumber(resultSet.getString(4));
        contact.setEmailAddress(resultSet.getString(5));

        return contact;
    }

    @Cacheable("contacts")
    public Contact findById(String id) {
        String sql = "select id, firstName, lastName, phoneNumber, emailAddress from contacts where id = ?";
        return (Contact) jdbcTemplate.queryForObject(sql, new Object[]{id}, new ContactRowMapper());
    }

    @CacheEvict("contacts")
    public void removeById(String id) {
        String sql = "delete from contacts where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public class ContactRowMapper implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            return getContact(rs);
        }
    }

    @CachePut(value = "contacts", key = "#result.id")
    public Contact save(Contact contact) {
        String sql = "insert into contacts(firstName, lastName, phoneNumber, emailAddress)" +
                " values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(sql, new String[]{"id"});
                        ps.setString(1, contact.getFirstName());
                        ps.setString(2, contact.getLastName());
                        ps.setString(3, contact.getPhoneNumber());
                        ps.setString(4, contact.getEmailAddress());
                        return ps;
                    }
                },
                keyHolder);

        contact.setId(keyHolder.getKey().longValue());

        return contact;
    }
}
