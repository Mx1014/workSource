//
// EvhPmsyCommunitiesDTO.h
// generated at 2016-04-29 18:56:00 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyCommunitiesDTO
//
@interface EvhPmsyCommunitiesDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSString* billTip;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

