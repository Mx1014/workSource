//
// EvhListUserRelatedOrganizationsCommand.h
// generated at 2016-04-12 15:02:18 
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

