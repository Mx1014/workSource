//
// EvhUserGroupActionData.h
// generated at 2016-03-28 15:56:07 
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

