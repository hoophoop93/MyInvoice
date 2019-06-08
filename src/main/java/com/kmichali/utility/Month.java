package com.kmichali.utility;

public enum Month {

    Styczen(1),
    Luty(2),
    Marzec(3),
    Kwiecien(4),
    Maj(5),
    Czerwiec(6),
    Lipiec(7),
    Sierpien(8),
    Wrzesien(9),
    Pazdziernik(10),
    Listopad(11),
    Grudzien(12);

    public int value;

     Month(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
