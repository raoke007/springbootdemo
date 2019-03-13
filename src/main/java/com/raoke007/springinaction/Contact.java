package com.raoke007.springinaction;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>description<p>
 *
 * @author raoke007
 * @date 2019/3/12 16:25
 */
@Data
public class Contact implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String emailAddress;

}
