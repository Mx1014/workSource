package com.everhomes.statistics.terminal;

import com.everhomes.filedownload.FileDownloadTaskHandler;
import com.everhomes.filedownload.FileDownloadTaskService;
import com.everhomes.filedownload.Task;
import com.everhomes.filedownload.TaskService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.filedownload.TaskStatus;
import com.everhomes.rest.statistics.terminal.*;
import com.everhomes.util.StringHelper;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.RowResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2018/3/26.
 */
@Component
public class StatTerminalFileDownloadTaskHandler implements FileDownloadTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatTerminalFileDownloadTaskHandler.class);

    @Autowired
    private StatTerminalProvider statTerminalProvider;

    @Autowired
    private NamespaceProvider namespaceProvider;

    @Autowired
    private StatTerminalService statTerminalService;

    @Autowired
    private FileDownloadTaskService fileDownloadTaskService;

    @Autowired
    private TaskService taskService;

    @Override
    public void beforeExecute(Map<String, Object> params) {

    }

    @Override
    public void execute(Map<String, Object> params) {
        String fileName = params.get("name").toString();
        String cmd = params.get("cmd").toString();

        String exportType = params.get("exportType").toString();

        CsFileLocationDTO csFileLocationDTO = null;
        switch (exportType) {
            case "exportTerminalAppVersionPieChart":
                csFileLocationDTO = exportTerminalAppVersionPieChart(fileName, (ListTerminalStatisticsByDayCommand)
                        StringHelper.fromJsonString(cmd, ListTerminalStatisticsByDayCommand.class));
                break;
            case "exportTerminalDayLineChart":
                csFileLocationDTO = exportTerminalDayLineChart(fileName, (ListTerminalStatisticsByDateCommand)
                        StringHelper.fromJsonString(cmd, ListTerminalStatisticsByDateCommand.class));
                break;
            case "exportTerminalHourLineChart":
                csFileLocationDTO = exportTerminalHourLineChart(params, cmd);
                break;
            default:
                LOGGER.error("ExportType should be recognized");
                break;
        }

        if (csFileLocationDTO != null) {
            Long taskId = (Long) params.get("taskId");

            Task task = taskService.findById(taskId);
            task.setProcess(100);
            task.setStatus(TaskStatus.SUCCESS.getCode());
            task.setResultString1(csFileLocationDTO.getUri());
            if(csFileLocationDTO.getSize() != null){
                task.setResultLong1(csFileLocationDTO.getSize().longValue());
            }
            taskService.updateTask(task);
        }
    }

    private CsFileLocationDTO exportTerminalHourLineChart(Map<String, Object> params, String cmdStr) {
        CsFileLocationDTO csFileLocationDTO = null;
        String fileType = params.get("fileType").toString();
        TerminalStatisticsChartCommand cmd = (TerminalStatisticsChartCommand)
                StringHelper.fromJsonString(cmdStr, TerminalStatisticsChartCommand.class);
        switch (fileType) {
            case "newUser":
                csFileLocationDTO = exportTerminalHourLineChartNewUser(cmd);
                break;
            case "start":
                csFileLocationDTO = exportTerminalHourLineChartStart(cmd);
                break;
            case "cumulativeUser":
                csFileLocationDTO = exportTerminalHourLineChartCumulativeUser(cmd);
                break;
            case "activeUser":
                csFileLocationDTO = exportTerminalHourLineChartActiveUser(cmd);
                break;
            default:
                LOGGER.error("FileType should be recognized");
                break;
        }
        return csFileLocationDTO;
    }

    private CsFileLocationDTO exportTerminalHourLineChartActiveUser(TerminalStatisticsChartCommand cmd) {
        Namespace namespace = namespaceProvider.findNamespaceById(cmd.getNamespaceId());
        String today = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());

        String fileName = namespace.getName() + "_时段分析_分时活跃_" + today + ".xlsx";
        return doExportTerminalHourLineChart(cmd, fileName, stat -> String.valueOf(stat.getActiveUserNumber()));
    }

    private CsFileLocationDTO doExportTerminalHourLineChart(TerminalStatisticsChartCommand cmd,
                                                            String fileName,
                                                            Function<TerminalHourStatistics, String> func) {
        List<RowResult> activeUserRows = new ArrayList<>();

        // cmd.getDates().sort(String::compareTo);
        for (String date : cmd.getDates()) {
            RowResult activeUserRow = new RowResult();
            activeUserRow.setA(date);

            int A = 65;
            List<TerminalHourStatistics> hourStatistics =
                    statTerminalProvider.listTerminalHourStatisticsByDay(date, cmd.getNamespaceId());
            for (TerminalHourStatistics stat : hourStatistics) {
                try {
                    A++;
                    Method setter = activeUserRow.getClass().getMethod("set" + String.format("%C", A), String.class);
                    setter.invoke(activeUserRow, func.apply(stat));
                } catch (Exception e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("RowResult setter error, stat = " + stat, e);
                    }
                }
            }
            activeUserRows.add(activeUserRow);
        }

        String[] propertyNames = {
                "A", "B", "C", "D", "E", "F", "G",
                "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U",
                "V", "W", "X", "Y"
        };
        String[] titleName = {
                "", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00",
                "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
                "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00",
                "22:00", "23:00", "24:00"
        };
        int[] columnSizes = {
                10, 8, 8, 8, 8, 8, 8,
                8, 8, 8, 8, 8, 8, 8,
                8, 8, 8, 8, 8, 8, 8,
                8, 8, 8, 8
        };

        ExcelUtils utils = new ExcelUtils(fileName, "DEFAULT");
        utils.setNeedSequenceColumn(false);
        OutputStream outputStream = utils.getOutputStream(propertyNames, titleName, columnSizes, activeUserRows);
         return fileDownloadTaskService.uploadToContenServer(fileName, outputStream, null);
    }

    private CsFileLocationDTO exportTerminalHourLineChartCumulativeUser(TerminalStatisticsChartCommand cmd) {
        Namespace namespace = namespaceProvider.findNamespaceById(cmd.getNamespaceId());
        String today = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());

        String fileName = namespace.getName() + "_时段分析_时段累计日活_" + today + ".xlsx";
        return doExportTerminalHourLineChart(cmd, fileName, stat -> String.valueOf(stat.getCumulativeUserNumber()));
    }

    private CsFileLocationDTO exportTerminalHourLineChartStart(TerminalStatisticsChartCommand cmd) {
        Namespace namespace = namespaceProvider.findNamespaceById(cmd.getNamespaceId());
        String today = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());

        String fileName = namespace.getName() + "_时段分析_启动次数_" + today + ".xlsx";
        return doExportTerminalHourLineChart(cmd, fileName, stat -> String.valueOf(stat.getStartNumber()));
    }

    private CsFileLocationDTO exportTerminalHourLineChartNewUser(TerminalStatisticsChartCommand cmd) {
        Namespace namespace = namespaceProvider.findNamespaceById(cmd.getNamespaceId());
        String today = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());

        String fileName = namespace.getName() + "_时段分析_新增用户_" + today + ".xlsx";
        return doExportTerminalHourLineChart(cmd, fileName, stat -> String.valueOf(stat.getNewUserNumber()));
    }

    /*private void exportTerminalHourLineChart(TerminalStatisticsChartCommand cmd) {
        List<RowResult> newUserRows = new ArrayList<>();
        List<RowResult> startRows = new ArrayList<>();
        List<RowResult> cumulativeUserRows = new ArrayList<>();
        List<RowResult> activeUserRows = new ArrayList<>();

        // cmd.getDates().sort(String::compareTo);
        for (String date : cmd.getDates()) {
            RowResult newUserRow = new RowResult();
            newUserRow.setA(date);
            RowResult startRow = new RowResult();
            startRow.setA(date);
            RowResult cumulativeUserRow = new RowResult();
            cumulativeUserRow.setA(date);
            RowResult activeUserRow = new RowResult();
            activeUserRow.setA(date);

            int A = 65;
            List<TerminalHourStatistics> hourStatistics =
                    statTerminalProvider.listTerminalHourStatisticsByDay(date, cmd.getNamespaceId());
            for (TerminalHourStatistics stat : hourStatistics) {
                try {
                    A++;
                    Method setter = newUserRow.getClass().getMethod("set" + String.format("%C", A), String.class);
                    setter.invoke(newUserRow, String.valueOf(stat.getStartNumber()));

                    setter = startRow.getClass().getMethod("set" + String.format("%C", A), String.class);
                    setter.invoke(startRow, String.valueOf(stat.getStartNumber()));

                    setter = cumulativeUserRow.getClass().getMethod("set" + String.format("%C", A), String.class);
                    setter.invoke(cumulativeUserRow, String.valueOf(stat.getCumulativeUserNumber()));

                    setter = activeUserRow.getClass().getMethod("set" + String.format("%C", A), String.class);
                    setter.invoke(activeUserRow, String.valueOf(stat.getActiveUserNumber()));
                } catch (Exception e) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("RowResult setter error, stat = " + stat, e);
                    }
                }
            }
            newUserRows.add(newUserRow);
            startRows.add(startRow);
            cumulativeUserRows.add(cumulativeUserRow);
            activeUserRows.add(activeUserRow);
        }

        String[] propertyNames = {
                "A", "B", "C", "D", "E", "F", "G",
                "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U",
                "V", "W", "X", "Y"
        };
        String[] titleName = {
                "", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00",
                "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00",
                "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00",
                "22:00", "23:00", "24:00"
        };
        int[] columnSizes = {
                10, 8, 8, 8, 8, 8, 8,
                8, 8, 8, 8, 8, 8, 8,
                8, 8, 8, 8, 8, 8, 8,
                8, 8, 8, 8
        };
        Namespace namespace = namespaceProvider.findNamespaceById(cmd.getNamespaceId());
        String today = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());

        String fileName = namespace.getName() + "_时段分析_新增用户_" + today + ".xlsx";
        ExcelUtils utils = new ExcelUtils(fileName, "DEFAULT");
        utils.setNeedSequenceColumn(false);
        OutputStream outputStream = utils.getOutputStream(propertyNames, titleName, columnSizes, newUserRows);
        fileDownloadTaskService.uploadToContenServer(fileName, outputStream);

        fileName = namespace.getName() + "_时段分析_启动次数_" + today + ".xlsx";
        utils = new ExcelUtils(fileName, "DEFAULT");
        utils.setNeedSequenceColumn(false);
        outputStream = utils.getOutputStream(propertyNames, titleName, columnSizes, startRows);
        fileDownloadTaskService.uploadToContenServer(fileName, outputStream);

        fileName = namespace.getName() + "_时段分析_时段累计日活_" + today + ".xlsx";
        utils = new ExcelUtils(fileName, "DEFAULT");
        utils.setNeedSequenceColumn(false);
        outputStream = utils.getOutputStream(propertyNames, titleName, columnSizes, cumulativeUserRows);
        fileDownloadTaskService.uploadToContenServer(fileName, outputStream);

        fileName = namespace.getName() + "_时段分析_分时活跃_" + today + ".xlsx";
        utils = new ExcelUtils(fileName, "DEFAULT");
        utils.setNeedSequenceColumn(false);
        outputStream = utils.getOutputStream(propertyNames, titleName, columnSizes, activeUserRows);
        fileDownloadTaskService.uploadToContenServer(fileName, outputStream);
    }*/

    private CsFileLocationDTO exportTerminalDayLineChart(String fileName, ListTerminalStatisticsByDateCommand cmd) {
        List<TerminalDayStatisticsDTO> rows = statTerminalService.listTerminalDayStatisticsByDate(
                cmd.getStartDate(), cmd.getEndDate(), cmd.getNamespaceId());

        String[] propertyNames = {"date", "newUserNumber", "activeUserNumber", "startNumber", "cumulativeUserNumber"};
        String[] titleName = {"日期", "新增用户", "活跃用户", "启动次数", "累计用户"};
        int[] columnSizes = {20, 20, 20, 20, 20};

        ExcelUtils utils = new ExcelUtils(fileName, "sheet");
        utils.setNeedSequenceColumn(false);
        OutputStream outputStream = utils.getOutputStream(propertyNames, titleName, columnSizes, rows);
        return fileDownloadTaskService.uploadToContenServer(fileName, outputStream, null);
    }

    private CsFileLocationDTO exportTerminalAppVersionPieChart(String fileName, ListTerminalStatisticsByDayCommand cmd) {
        List<TerminalAppVersionStatisticsDTO> dtoList = statTerminalService.listTerminalAppVersionStatistics(cmd.getDate(), cmd.getNamespaceId());

        List<RowResult> rows = buildTerminalAppVersionPieChartRowResult(dtoList);

        String[] propertyNames = {"A", "B", "C", "D", "E"};
        String[] titleName = {
                "应用版本",
                String.format("版本累计用户\n(截止%s)", cmd.getDate()),
                String.format("版本累计用户占比\n(截止%s)", cmd.getDate()),
                String.format("版本活跃用户\n(%s)", cmd.getDate()),
                String.format("版本活跃用户占比\n(%s)", cmd.getDate()),
        };
        int[] columnSizes = {20, 20, 20, 20, 20};

        ExcelUtils utils = new ExcelUtils(fileName, "DEFAULT");
        utils.setNeedSequenceColumn(false);
        OutputStream outputStream = utils.getOutputStream(propertyNames, titleName, columnSizes, rows);
        return fileDownloadTaskService.uploadToContenServer(fileName, outputStream, null);
    }

    private List<RowResult> buildTerminalAppVersionPieChartRowResult(List<TerminalAppVersionStatisticsDTO> dtoList) {
        return dtoList.stream().map(r -> {
            RowResult row = new RowResult();
            row.setA(r.getAppVersion());
            row.setB(String.valueOf(r.getCumulativeUserNumber()));
            row.setC(String.format("%s%%", r.getVersionCumulativeRate()));
            row.setD(String.valueOf(r.getActiveUserNumber()));
            row.setE(String.format("%s%%", r.getVersionActiveRate()));
            return row;
        }).collect(Collectors.toList());
    }

    @Override
    public void commit(Map<String, Object> params) {

    }

    @Override
    public void afterExecute(Map<String, Object> params) {

    }
}
