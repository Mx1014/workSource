//
// EvhOpenDoorActionData.h
// generated at 2016-03-31 10:18:19 
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

