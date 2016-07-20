//
// EvhQualityInspectionTaskStatus.h
//


///////////////////////////////////////////////////////////////////////////////
// EvhQualityInspectionTaskStatus
//
typedef enum {

    EvhQualityInspectionTaskStatus_NONE = 0, 
    EvhQualityInspectionTaskStatus_WAITING_FOR_EXECUTING = 1, 
    EvhQualityInspectionTaskStatus_RECTIFING = 2, 
    EvhQualityInspectionTaskStatus_RECTIFIED_AND_WAITING_APPROVAL = 3, 
    EvhQualityInspectionTaskStatus_RECTIFY_CLOSED_AND_WAITING_APPROVAL = 4, 
    EvhQualityInspectionTaskStatus_CLOSED = 5

} EvhQualityInspectionTaskStatus;

///////////////////////////////////////////////////////////////////////////////

