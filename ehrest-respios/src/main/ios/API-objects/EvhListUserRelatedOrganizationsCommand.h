//
// EvhListUserRelatedOrganizationsCommand.h
// generated at 2016-03-25 19:05:20 
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

