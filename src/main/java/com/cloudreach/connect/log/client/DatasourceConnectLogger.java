package com.cloudreach.connect.log.client;

import com.cloudreach.alumina.utils.HDBTask;
import com.cloudreach.alumina.utils.UT;
import com.cloudreach.connect.api.LogService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

@RequiredArgsConstructor
public class DatasourceConnectLogger implements LogService {

    private final QueryRunner queryRunner = new QueryRunner();
    private final ResultSetHandler<Long> longHandler = new ScalarHandler<>();

    private final String schema;
    private final String dataSource;
    private final String appKey;

    @Override
    public void info(String s) {
        log(s, Level.INFO);
    }

    @Override
    public void error(String s) {
        log(s, Level.SEVERE);
    }

    @Override
    public void error(String s, Exception e) {
        StringBuilder stringBuilder = new StringBuilder();
        if (s != null) {
            stringBuilder.append(s).append("\n");
        }
        if (e != null) {
            stringBuilder.append(ExceptionUtils.getStackTrace(e));
        }
        String message = StringUtils.trimToNull(stringBuilder.toString());
        log(message, Level.SEVERE);
    }

    @SneakyThrows
    private void log(final String message, final Level level) {
        if (message != null && level != null) {
            UT.DB(dataSource, new HDBTask() {
                @Override
                public void run(Connection db) throws Exception {
                    long idMessage = create(db, message);
                    create(db, idMessage, level.intValue(), new Timestamp(System.currentTimeMillis()));
                }
            });
        }
    }

    private long create(Connection db, String message) throws SQLException {
        String query = "INSERT INTO " + schema + ".message (message) VALUES (?)";
        Object[] params = {message};
        return queryRunner.insert(db, query, longHandler, params);
    }

    private long create(Connection db, long idMessage, long level, Timestamp date) throws SQLException {
        String query = "INSERT INTO " + schema + ".log_entry (id_message, app_key, level, date) " +
                "VALUES (?, ?, ?, ?)";
        Object[] params = {idMessage, appKey, level, date};
        return queryRunner.insert(db, query, longHandler, params);
    }

}
