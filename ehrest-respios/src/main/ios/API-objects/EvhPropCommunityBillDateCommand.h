//
// EvhPropCommunityBillDateCommand.h
// generated at 2016-04-07 14:16:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropCommunityBillDateCommand
//
@interface EvhPropCommunityBillDateCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* dateStr;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

