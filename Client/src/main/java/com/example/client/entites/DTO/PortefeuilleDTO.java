package com.example.client.entites.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortefeuilleDTO {
    private Long idActionnaire;
    private Long idAction;
    private int quantite;
}
