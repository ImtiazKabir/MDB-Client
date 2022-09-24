module com.mdb.movieclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens com.mdb.movieclient to javafx.fxml;
    exports com.mdb.movieclient;
    exports archive.entity;
    opens archive.entity to javafx.fxml;
    exports archive.info;
    opens archive.info to javafx.fxml;
}