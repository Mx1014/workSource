//
// EvhReportRectifyResultCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAttachmentDescriptor.h"

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

// item type EvhAttachmentDescriptor*
@property(nonatomic, strong) NSMutableArray* attachments;

@property(nonatomic, copy) NSString* message;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

