//
// EvhReportVerificationResultCommand.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReportVerificationResultCommand
//
@interface EvhReportVerificationResultCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* taskId;

@property(nonatomic, copy) NSNumber* verificationResult;

// item type EvhAttachmentDescriptor*
@property(nonatomic, strong) NSMutableArray* attachments;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSString* operatorType;

@property(nonatomic, copy) NSNumber* operatorId;

@property(nonatomic, copy) NSString* message;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

