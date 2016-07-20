//
// EvhQualityInspectionTaskResult.h
//


///////////////////////////////////////////////////////////////////////////////
// EvhQualityInspectionTaskResult
//
typedef enum {

    EvhQualityInspectionTaskResult_NONE = 0, 
    EvhQualityInspectionTaskResult_INSPECT_OK = 1, 
    EvhQualityInspectionTaskResult_INSPECT_CLOSE = 2, 
    EvhQualityInspectionTaskResult_RECTIFIED_OK = 3, 
    EvhQualityInspectionTaskResult_RECTIFY_CLOSED = 4, 
    EvhQualityInspectionTaskResult_INSPECT_DELAY = 5, 
    EvhQualityInspectionTaskResult_RECTIFY_DELAY = 6, 
    EvhQualityInspectionTaskResult_CORRECT_DELAY = 7, 
    EvhQualityInspectionTaskResult_RECTIFIED_OK_AND_WAITING_APPROVAL = 11, 
    EvhQualityInspectionTaskResult_RECTIFY_CLOSED_AND_WAITING_APPROVAL = 12

} EvhQualityInspectionTaskResult;

///////////////////////////////////////////////////////////////////////////////

