module Lab {
    requires transitive javafx.controls;
    exports calendar.view_controller.fx;  // UI access by client
    exports edu.rit.cs;                   // utilities
    exports calendar.model;               // access needed by UI
    // access needed by UI
}