//
// EvhUserTokenCommandResponse.h
// generated at 2016-03-25 17:08:11 
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

