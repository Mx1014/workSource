//
// EvhListPropApartmentsByKeywordCommand.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropApartmentsByKeywordCommand
//
@interface EvhListPropApartmentsByKeywordCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* keyword;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

