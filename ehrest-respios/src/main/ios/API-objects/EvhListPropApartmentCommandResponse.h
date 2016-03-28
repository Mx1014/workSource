//
// EvhListPropApartmentCommandResponse.h
// generated at 2016-03-25 19:05:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPropAddressMappingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropApartmentCommandResponse
//
@interface EvhListPropApartmentCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhPropAddressMappingDTO*
@property(nonatomic, strong) NSMutableArray* mappings;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

