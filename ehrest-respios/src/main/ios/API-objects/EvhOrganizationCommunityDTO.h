//
// EvhOrganizationCommunityDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationCommunityDTO
//
@interface EvhOrganizationCommunityDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* communityName;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSString* areaName;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* communityType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

