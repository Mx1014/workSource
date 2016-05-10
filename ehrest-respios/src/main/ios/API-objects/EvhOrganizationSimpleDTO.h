//
// EvhOrganizationSimpleDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationSimpleDTO
//
@interface EvhOrganizationSimpleDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* organizationType;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* communityName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

