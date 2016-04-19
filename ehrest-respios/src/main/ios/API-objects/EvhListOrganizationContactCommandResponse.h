//
// EvhListOrganizationContactCommandResponse.h
// generated at 2016-04-19 12:41:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrganizationContactCommandResponse
//
@interface EvhListOrganizationContactCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhOrganizationContactDTO*
@property(nonatomic, strong) NSMutableArray* members;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

