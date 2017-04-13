package com.everhomes.kafka.logs;

import ch.qos.logback.classic.spi.ClassPackagingData;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.CoreConstants;

/**
 * Created by Administrator on 2017/4/10.
 */
public class CustomThrowableProxyUtil {
    public static final int REGULAR_EXCEPTION_INDENT = 1;
    public static final int SUPPRESSED_EXCEPTION_INDENT = 1;
    private static final int BUILDER_CAPACITY = 2048;

    public static final String LINE_SEPARATOR = "<br/>";
    public static String asString(IThrowableProxy tp) {
        StringBuilder sb = new StringBuilder(BUILDER_CAPACITY);

        recursiveAppend(sb, null, REGULAR_EXCEPTION_INDENT, tp);

        return sb.toString();
    }

    private static void recursiveAppend(StringBuilder sb, String prefix, int indent, IThrowableProxy tp) {
        if(tp == null)
            return;
        subjoinFirstLine(sb, prefix, indent, tp);
        sb.append(LINE_SEPARATOR + CoreConstants.LINE_SEPARATOR);
        subjoinSTEPArray(sb, indent, tp);
        IThrowableProxy[] suppressed = tp.getSuppressed();
        if(suppressed != null) {
            for(IThrowableProxy current : suppressed) {
                recursiveAppend(sb, CoreConstants.SUPPRESSED, indent + SUPPRESSED_EXCEPTION_INDENT, current);
            }
        }
        recursiveAppend(sb, CoreConstants.CAUSED_BY, indent, tp.getCause());
    }

    public static void indent(StringBuilder buf, int indent) {
        for(int j = 0; j < indent; j++) {
            buf.append(CoreConstants.TAB);
            buf.append("&nbsp;&nbsp;");

        }
    }

    private static void subjoinFirstLine(StringBuilder buf, String prefix, int indent, IThrowableProxy tp) {
        indent(buf, indent - 1);
        if (prefix != null) {
            buf.append(prefix);
        }
        subjoinExceptionMessage(buf, tp);
    }

    public static void subjoinPackagingData(StringBuilder builder, StackTraceElementProxy step) {
        if (step != null) {
            ClassPackagingData cpd = step.getClassPackagingData();
            if (cpd != null) {
                if (!cpd.isExact()) {
                    builder.append(" ~[");
                } else {
                    builder.append(" [");
                }

                builder.append(cpd.getCodeLocation()).append(':').append(
                        cpd.getVersion()).append(']');
            }
        }
    }

    public static void subjoinSTEP(StringBuilder sb, StackTraceElementProxy step) {
        sb.append(step.toString());
        subjoinPackagingData(sb, step);
    }

    /**
     * @param sb The StringBuilder the STEPs are appended to.
     * @param indentLevel indentation level used for the STEPs, usually REGULAR_EXCEPTION_INDENT.
     * @param tp the IThrowableProxy containing the STEPs.
     */
    public static void subjoinSTEPArray(StringBuilder sb, int indentLevel, IThrowableProxy tp) {
        StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
        int commonFrames = tp.getCommonFrames();

        for (int i = 0; i < stepArray.length - commonFrames; i++) {
            StackTraceElementProxy step = stepArray[i];
            indent(sb, indentLevel);
            subjoinSTEP(sb, step);
            sb.append(LINE_SEPARATOR + CoreConstants.LINE_SEPARATOR);
        }

        if (commonFrames > 0) {
            indent(sb, indentLevel);
            sb.append("... ").append(commonFrames).append(" common frames omitted")
                    .append(LINE_SEPARATOR + CoreConstants.LINE_SEPARATOR);
        }

    }

    private static void subjoinExceptionMessage(StringBuilder buf, IThrowableProxy tp) {
        buf.append(tp.getClassName()).append(": ").append(tp.getMessage());
    }
}
