//
// EvhUserTokenCommandResponse.h
// generated at 2016-04-06 19:10:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhUserInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserTokenCommandResponse
//
@interface EvhUserTokenCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhUserInfo* user;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

