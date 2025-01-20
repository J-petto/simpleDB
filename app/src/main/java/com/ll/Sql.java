package com.ll;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class Sql {
    private final Connection connection;

    private final StringBuilder query = new StringBuilder();
    private final ArrayList<Object> parameter = new ArrayList<>();


    public Sql(Connection connection) {
        this.connection = connection;
    }

    public long insert() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(query), Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < parameter.size(); i++) {
                preparedStatement.setObject(i + 1, parameter.get(i));
            }

            preparedStatement.executeUpdate();

            clearQuery();
            parameter.clear();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);  // AUTO_INCREMENT로 생성된 ID 반환
                } else {
                    throw new Exception("Creating user failed, no ID obtained.");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int update() {
        int result;

        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(query))) {
            for (int i = 0; i < parameter.size(); i++) {
                preparedStatement.setObject(i + 1, parameter.get(i));
            }

            result = preparedStatement.executeUpdate();
            clearQuery();
            parameter.clear();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public int delete() {
        return update();
    }

    public List<Map<String, Object>> selectRows() {
        List<Map<String, Object>> result = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(query))) {
            for (int i = 0; i < parameter.size(); i++) {
                preparedStatement.setObject(i + 1, parameter.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Map<String, Object> map = new HashMap<>();

                    map.put("id", resultSet.getLong("id"));
                    map.put("title", resultSet.getString("title"));
                    map.put("body", resultSet.getString("body"));
                    map.put("createdDate", resultSet.getTimestamp("createdDate").toLocalDateTime());
                    map.put("modifiedDate", resultSet.getTimestamp("modifiedDate").toLocalDateTime());
                    map.put("isBlind", resultSet.getBoolean("isBlind"));

                    result.add(map);
                } else {
                    System.out.println("No record found with ID: " + resultSet);
                }
            }

            clearQuery();
            parameter.clear();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public Map<String, Object> selectRow() {
        Map<String, Object> result = new HashMap<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(query))) {
            for (int i = 0; i < parameter.size(); i++) {
                preparedStatement.setObject(i + 1, parameter.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    result.put("id", resultSet.getLong("id"));
                    result.put("title", resultSet.getString("title"));
                    result.put("body", resultSet.getString("body"));
                    result.put("createdDate", resultSet.getTimestamp("createdDate").toLocalDateTime());
                    result.put("modifiedDate", resultSet.getTimestamp("modifiedDate").toLocalDateTime());
                    result.put("isBlind", resultSet.getBoolean("isBlind"));
                } else {
                    System.out.println("No record found with ID: " + resultSet);
                }
            }

            clearQuery();
            parameter.clear();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public LocalDateTime selectDatetime() {
        LocalDateTime result = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(query))) {
            for (int i = 0; i < parameter.size(); i++) {
                preparedStatement.setObject(i + 1, parameter.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Timestamp now = (Timestamp) resultSet.getObject(1);
                    result = now.toLocalDateTime();
                } else {
                    System.out.println("No record found with ID: " + resultSet);
                }
            }

            clearQuery();
            parameter.clear();
            return result;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public String selectString() {
        String result = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(query))) {
            for (int i = 0; i < parameter.size(); i++) {
                preparedStatement.setObject(i + 1, parameter.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getString(1);
                } else {
                    System.out.println("No record found with ID: " + resultSet);
                }
            }

            clearQuery();
            parameter.clear();
            return result;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public Boolean selectBoolean() {
        boolean result = false;

        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(query))) {
            for (int i = 0; i < parameter.size(); i++) {
                preparedStatement.setObject(i + 1, parameter.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getBoolean(1);
                } else {
                    System.out.println("No record found with ID: " + resultSet);
                }
            }

            clearQuery();
            parameter.clear();
            return result;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public long selectLong() {
        long result = 0L;

        try (PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(query))) {
            for (int i = 0; i < parameter.size(); i++) {
                preparedStatement.setObject(i + 1, parameter.get(i));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getLong(1);
                } else {
                    System.out.println("No record found with ID: " + resultSet);
                }
            }

            clearQuery();
            parameter.clear();
            return result;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed", e);
        }
    }

    public Sql append(String arg) {
        checkSpace(arg);
        return this;
    }

    public Sql append(String arg, Object... sets) {
        checkSpace(arg);
        this.parameter.addAll(Arrays.asList(sets));
        return this;
    }

    public Sql appendIn(String arg, Object... inParams) {
        checkSpace(arg);
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Query는 null이거나 빈 값일 수 없습니다.");
        }
        if (inParams == null || inParams.length == 0) {
            throw new IllegalArgumentException("IN 조건에는 하나 이상의 값이 필요합니다.");
        }

        // ?를 getPlaceholders로 대체
        String placeholder = getPlaceholders(inParams.length);
        String replacedQuery = query.toString().replaceFirst("\\?", placeholder);

        query.append(replacedQuery).append(" ");
        parameter.addAll(Arrays.asList(inParams));
        return this;
    }

    // 플레이스홀더 생성 (?, ?, ?)
    private String getPlaceholders(int count) {
        return String.join(", ", Collections.nCopies(count, "?"));
    }

    public Sql appendIn(String... args) {
        return this;
    }

    public void checkSpace(String query) {
        if (this.query.isEmpty()) {
            this.query.append(query);
        } else {
            this.query.append(" ").append(query);
        }
    }

    public List<Long> selectLongs() {
        return null;
    }

    public void clearQuery() {
        query.setLength(0);
    }


    public List<Article> selectRows(Class<?> getClass) {
        return null;
    }



    public Article selectRow(Class<?> getClass) {
        return null;
    }





}
