//
// EvhListUserRelatedOrganizationsCommand.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserRelatedOrganizationsCommand
//
@interface EvhListUserRelatedOrganizationsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* organiztionType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

