//
// EvhListGroupWaitingApprovalsCommand.h
// generated at 2016-04-06 19:10:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListGroupWaitingApprovalsCommand
//
@interface EvhListGroupWaitingApprovalsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

