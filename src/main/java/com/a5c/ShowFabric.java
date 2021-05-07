package com.a5c;

import com.a5c.data.Transform;
import com.a5c.data.Unload;
import com.a5c.db.dbConnect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class ShowFabric {
    /**
     * Variable to store the connection to DataBase.
     * Public - It can be access by everyone.
     * Static - Never changes.
     */
    private final dbConnect db;

    /**
     * SpringBoot and DB connection init.
     * @param args - args from system.
     */
    public static void main(String[] args) {
        SpringApplication.run(ShowFabric.class, args);
    }

    /**
     * Constructor to init DB Connection, if necessary.
     */
    public ShowFabric() {
        db = new dbConnect();
    }

    /**
     * Method to show a default String.
     * @return default String.
     */
    @GetMapping("")
    public String defaultroot() { return "A5C - II 2020/2021 - Francisco Caetano, Francisco Damas, Guilherme Pinheiro, Tomás Araújo."; }

    @GetMapping("transform")
    public String Transform() throws SQLException {
        return db.getTransform();
    }

    @GetMapping("transformDOING")
    public String ElapseTransform() throws SQLException {
        return db.getElapseTransform();
    }

    @GetMapping("transformDONE")
    public String EndTransform() throws SQLException {
        return db.getEndTransform();
    }

    @GetMapping("unload")
    public String Unload() throws SQLException {
        return db.getUnload();
    }

    @GetMapping("unloadDONE")
    public String EndUnload() throws SQLException {
        return db.getUnloadDone();
    }

    @GetMapping("warehouse")
    public String CurrentStores() throws SQLException {
        return db.getCurrentStores();
    }

    @GetMapping("machines")
    public String Machines() throws SQLException {
        return db.getMachines();
    }

    @GetMapping("pushers")
    public String Pushers() throws SQLException {
        return db.getPushers();
    }

    @GetMapping("exceptedTT")
    public String ExceptedTT() throws SQLException {
        return db.getExcepted();
    }

    @RequestMapping("addTransform/{od}/{from}/{to}/{quant}/{md}//{pen}")
    public String addTransform(@PathVariable int od, @PathVariable int from, @PathVariable int to, @PathVariable int quant, @PathVariable int md, @PathVariable int pen) {
        try {
            db.addTransform(new Transform(od,from,to,quant,0,md,pen));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "Added to transform's list TO DO.";
    }

    @RequestMapping("addUnload/{od}/{piece}/{dest}/{quant}")
    public String addUnload(@PathVariable int od, @PathVariable int piece, @PathVariable int dest, @PathVariable int quant) {
        try {
            db.addUnload(new Unload(od,piece,dest,quant));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "Added to unload's list TO DO.";
    }

}
