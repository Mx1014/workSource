//
// EvhReportVerificationResultCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhForumAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReportVerificationResultCommand
//
@interface EvhReportVerificationResultCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* taskId;

@property(nonatomic, copy) NSNumber* verificationResult;

// item type EvhForumAttachmentDescriptor*
@property(nonatomic, strong) NSMutableArray* attachments;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSString* operatorType;

@property(nonatomic, copy) NSNumber* operatorId;

@property(nonatomic, copy) NSString* message;

@property(nonatomic, copy) NSNumber* categoryId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

