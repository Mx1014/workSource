//
// EvhUserGroupActionData.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserGroupActionData
//
@interface EvhUserGroupActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* privateFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

