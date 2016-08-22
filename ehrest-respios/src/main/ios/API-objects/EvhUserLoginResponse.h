//
// EvhUserLoginResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhUserLoginDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserLoginResponse
//
@interface EvhUserLoginResponse
    : NSObject<EvhJsonSerializable>


// item type EvhUserLoginDTO*
@property(nonatomic, strong) NSMutableArray* logins;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

