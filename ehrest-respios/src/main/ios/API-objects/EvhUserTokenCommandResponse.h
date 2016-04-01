//
// EvhUserTokenCommandResponse.h
// generated at 2016-04-01 15:40:22 
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

