package edu.volkov.restmanager.to;

import edu.volkov.restmanager.HasId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseTo implements HasId {

    protected Integer id;
}
