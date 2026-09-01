package com.email.email_writer.DTO;

import lombok.Data;

@Data
public class EmailRequest {

    private String emailcontent;
    private String tone;
    private String length;
}
