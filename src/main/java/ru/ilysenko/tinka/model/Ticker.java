/*
 * The GNU Affero General Public License v3.0 (AGPL-3.0)
 *
 * Copyright (c) 2020 Ilya Lysenko
 *
 * Permissions of this strongest copyleft license are conditioned on making available complete source code of licensed
 * works and modifications, which include larger works using a licensed work, under the same license.
 * Copyright and license notices must be preserved. Contributors provide an express grant of patent rights.
 * When a modified version is used to provide a service over a network,  the complete source code of the modified
 * version must be made available.
 */
package ru.ilysenko.tinka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static ru.ilysenko.tinka.model.ClassCode.SPBXM;
import static ru.ilysenko.tinka.model.ClassCode.TQBR;

@Getter
@AllArgsConstructor
public enum Ticker {
    APPLE("AAPL", SPBXM),
    TESLA("TSLA", SPBXM),
    ZOOM("ZM", SPBXM),
    SBER("SBER", TQBR),
    GMNK("GMNK", TQBR),
    TINKOFF("TCSG", TQBR);

    private final String value;
    private final ClassCode classCode;
}
