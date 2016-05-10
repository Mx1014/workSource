//
// EvhCommunityManagerDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityManagerDTO
//
@interface EvhCommunityManagerDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* managerId;

@property(nonatomic, copy) NSString* managerName;

@property(nonatomic, copy) NSString* managerPhone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

