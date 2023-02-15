package com.andela.XMLFileUploadToMySQLDb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Index;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "xml_data")
public class XMLData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "newspaper_name")
    @Index(name = "newspaper_name_index")
    private String newspaperName;

    @Column(name = "screen_width")
    @Index(name = "screen_width_index")
    private String screenWidth;

    @Column(name = "screen_height")
    @Index(name = "screen_height_index")
    private String screenHeight;

    @Column(name = "screen_dpi")
    @Index(name = "screen_dpi_index")
    private String screenDpi;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_time")
    @Index(name = "upload_time_index")
    private Timestamp uploadTime;

    @Column(name = "filename")
    @Index(name = "filename_index")
    private String filename;

}