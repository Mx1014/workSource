//
// EvhListAesUserKeyByUserResponse.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAesUserKeyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAesUserKeyByUserResponse
//
@interface EvhListAesUserKeyByUserResponse
    : NSObject<EvhJsonSerializable>


// item type EvhAesUserKeyDTO*
@property(nonatomic, strong) NSMutableArray* aesUserKeys;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

