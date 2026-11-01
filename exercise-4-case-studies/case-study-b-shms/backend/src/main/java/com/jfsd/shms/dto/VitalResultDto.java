package com.jfsd.shms.dto;

import java.util.List;

/**
 * Answer to "record a reading": the saved reading and the alerts it raised.
 */
public record VitalResultDto(VitalDto reading, List<AlertDto> alerts) {
}
