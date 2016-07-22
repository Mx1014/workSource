//
// EvhReportRectifyResultCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhForumAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReportRectifyResultCommand
//
@interface EvhReportRectifyResultCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* taskId;

@property(nonatomic, copy) NSNumber* rectifyResult;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSString* operatorType;

@property(nonatomic, copy) NSNumber* operatorId;

// item type EvhForumAttachmentDescriptor*
@property(nonatomic, strong) NSMutableArray* attachments;

@property(nonatomic, copy) NSString* message;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

