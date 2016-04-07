//
// EvhJoinVideoConfResponse.h
// generated at 2016-04-07 15:16:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhJoinVideoConfResponse
//
@interface EvhJoinVideoConfResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* joinUrl;

@property(nonatomic, copy) NSString* condId;

@property(nonatomic, copy) NSString* password;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

