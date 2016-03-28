//
// EvhAclinkUserResponse.h
// generated at 2016-03-25 19:05:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAclinkUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkUserResponse
//
@interface EvhAclinkUserResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhAclinkUserDTO*
@property(nonatomic, strong) NSMutableArray* users;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

