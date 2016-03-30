//
// EvhJoinVideoConfResponse.h
// generated at 2016-03-30 10:13:07 
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

