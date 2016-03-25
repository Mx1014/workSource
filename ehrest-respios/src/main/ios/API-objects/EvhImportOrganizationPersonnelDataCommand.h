//
// EvhImportOrganizationPersonnelDataCommand.h
// generated at 2016-03-25 11:43:34 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhImportOrganizationPersonnelDataCommand
//
@interface EvhImportOrganizationPersonnelDataCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

