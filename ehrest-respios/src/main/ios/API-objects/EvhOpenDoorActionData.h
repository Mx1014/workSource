//
// EvhOpenDoorActionData.h
// generated at 2016-04-07 14:16:30 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpenDoorActionData
//
@interface EvhOpenDoorActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* vender;

@property(nonatomic, copy) NSNumber* remote;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

