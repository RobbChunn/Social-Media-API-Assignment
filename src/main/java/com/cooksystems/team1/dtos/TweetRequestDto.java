package com.cooksystems.team1.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetRequestDto {

    private String content;
    private CredentialsDto credentials;
}
